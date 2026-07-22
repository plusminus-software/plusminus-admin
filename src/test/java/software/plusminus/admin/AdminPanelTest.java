package software.plusminus.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import software.plusminus.admin.controllers.TestController;
import software.plusminus.admin.model.DataAction;
import software.plusminus.browser.BrowserSettings;
import software.plusminus.browser.Element;
import software.plusminus.browser.Finder;
import software.plusminus.test.BrowserTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.apache.commons.compress.utils.CharsetNames.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.fail;

public class AdminPanelTest extends BrowserTest {

    private static final List<List<String>> CSV = readCsv("/type-table.csv");
    private static final List<String> HEADER_ROW = CSV.get(0);
    private static final List<String> EXISTING_ROW_1 = CSV.get(1);
    private static final List<String> EXISTING_ROW_2 = CSV.get(2);
    private static final List<String> NEW_ROW = CSV.get(3);
    private static final List<String> EMPTY_ROW = Stream.generate(() -> "").limit(15)
            .collect(Collectors.toList());

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestController testController;

    @Before
    public void openAdminPanel() {
        login("TestUser", "admin");
        go("/admin");
    }

    @Override
    protected BrowserSettings settings() {
        return super.settings()
                .beforePageLoads(() -> testController.refreshData());
    }

    @Test
    public void navPanel() {
        List<Element> types = find("nav.tabs li").includeHidden().many(2);
        assertThat(types).extracting(Element::text)
                .containsExactly("TestEntity", "ErrorEntity");
    }

    @Test
    public void typeTable() {
        List<Element> columns = find("table thead th").many(HEADER_ROW.size());
        assertThat(columns).extracting(Element::text)
                .containsExactlyElementsOf(HEADER_ROW);

        List<Element> rows = find("table tbody tr").many(2);
        List<Element> cells1 = rows.get(0).find("td").includeHidden().many(EXISTING_ROW_1.size());
        List<Element> cells2 = rows.get(1).find("td").includeHidden().many(EXISTING_ROW_2.size());

        assertThat(cells1).extracting(Element::text)
                .containsExactlyElementsOf(EXISTING_ROW_1);
        assertThat(cells2).extracting(Element::text)
                .containsExactlyElementsOf(EXISTING_ROW_2);
    }

    @Test
    public void addEntity() {
        clickTestEntityNav();
        find("button", "Add").one().click();
        testModal(EMPTY_ROW, NEW_ROW, DataAction.CREATE);
        checkTable(Arrays.asList(EXISTING_ROW_1, EXISTING_ROW_2, NEW_ROW));
    }

    @Test
    public void editEntity() {
        testRow(EXISTING_ROW_2, NEW_ROW, DataAction.UPDATE,
                Arrays.asList(EXISTING_ROW_1, NEW_ROW));
    }

    @Test
    public void editEntityWithoutChanges() {
        testRow(EXISTING_ROW_2, null, DataAction.UPDATE,
                Arrays.asList(EXISTING_ROW_1, EXISTING_ROW_2));
    }

    @Test
    public void cloneEntity() {
        testRow(EXISTING_ROW_2, NEW_ROW, DataAction.CLONE,
                Arrays.asList(EXISTING_ROW_1, EXISTING_ROW_2, NEW_ROW));
    }

    @Test
    public void cloneEntityWithoutChanges() {
        testRow(EXISTING_ROW_2, null, DataAction.CLONE,
                Arrays.asList(EXISTING_ROW_1, EXISTING_ROW_2, EXISTING_ROW_2));
    }

    @Test
    public void deleteEntity() {
        testRow(EXISTING_ROW_2, null, DataAction.DELETE,
                Collections.singletonList(EXISTING_ROW_1));
    }

    private void clickTestEntityNav() {
        find("a", "TestEntity").one().click();
    }

    private void testRow(List<String> currentValues, List<String> newValues,
                         DataAction action, List<List<String>> tableValuesAfterAction) {
        int index = CSV.indexOf(currentValues) - 1;
        clickTestEntityNav();
        find("button", action.toString()).many(2)
                .get(index).click();
        testModal(currentValues, newValues, action);
        checkTable(tableValuesAfterAction);
    }

    private void testModal(List<String> initValues, @Nullable List<String> newValues,
                           DataAction action) {
        checkModalLabels(action);
        if (action == DataAction.DELETE) {
            checkValuesInModalOnDelete(initValues);
        } else {
            checkValuesInModal(initValues, action);
        }

        setValuesInModal(newValues);
        find(".modal").one()
                .find("button", action.toString()).one()
                .click();
        find(".modal").includeHidden().none();
    }

    private void checkTable(List<List<String>> tableValues) {
        List<Element> rows = find("table tbody tr").many(tableValues.size());
        for (int i = 0; i < tableValues.size(); i++) {
            List<String> rowValues = new ArrayList<>(tableValues.get(i));
            rowValues.set(0, Integer.toString(i)); // set correct id
            Element row = rows.get(i);
            List<Element> cells = row.find("td").many(rowValues.size());
            assertThat(cells).extracting(Element::text).containsExactlyElementsOf(rowValues);
        }
    }

    private void checkModalLabels(DataAction action) {
        List<String> columnHeaders = new ArrayList<>(HEADER_ROW.subList(0, 14));
        if (action != DataAction.DELETE) {
            columnHeaders.add(9, "EmbeddedInt");
            columnHeaders.add(10, "EmbeddedString");
            unhide(findByLabel("Embedded", ".card"), "Create");
        }
        find(".collapse a").max(1).forEach(Element::click);
        List<Element> labels = find(".modal-card-body label").many(columnHeaders.size());
        assertThat(labels).extracting(Element::text)
                .containsExactlyElementsOf(columnHeaders);
    }

    private void checkValuesInModal(List<String> values, DataAction dataAction) {
        if (dataAction != DataAction.CLONE) {
            checkInput("Id", values.get(0));
        }
        checkCheckbox("Checkbox", values.get(1));
        checkInput("Number", values.get(2));
        checkInputTags("Numbers", jsonToStrings(values.get(3)));
        checkInput("String", values.get(4));
        checkInputTags("Strings", jsonToStrings(values.get(5)));
        checkSelect("TestEnum", values.get(6));
        checkDropdown("Enums", values.get(7));

        Element embedded = findByLabel("Embedded", ".card");
        checkInput(embedded, "EmbeddedInt", values.get(8));
        checkInput(embedded, "EmbeddedString", embeddedStringFor(values.get(8)));

        Element embeddeds = findByLabel("Embeddeds", ".card");
        checkTags(embeddeds, jsonToStrings(values.get(9)));

        Element relation = findByLabel("Relation", ".card");
        checkTags(relation, values.get(10));

        Element relations = findByLabel("Relations", ".card");
        checkTags(relations, jsonToStrings(values.get(11)));

        checkInput("ParentInt", values.get(12));
        checkInput("ParentString", values.get(13));
    }

    private void checkValuesInModalOnDelete(List<String> values) {
        checkSpan("Id", values.get(0));
        checkSpan("Checkbox", values.get(1));
        checkSpan("Number", values.get(2));
        checkSpan("Numbers", values.get(3));
        checkSpan("String", values.get(4));
        checkSpan("Strings", values.get(5));
        checkSpan("TestEnum", values.get(6));
        checkSpan("Enums", values.get(7));
        checkEmbeddedSpan(values.get(8));
        checkSpan("Embeddeds", values.get(9));
        checkSpan("Relation", values.get(10));
        checkSpan("Relations", values.get(11));
        checkSpan("ParentInt", values.get(12));
        checkSpan("ParentString", values.get(13));
    }

    private void setValuesInModal(@Nullable List<String> values) {
        if (values == null) {
            return;
        } else if (values != NEW_ROW) {
            fail("Not implemented yet");
        }
        setCheckbox("Checkbox", true);
        setInput("Number", "3");
        setInputTags("Numbers", "30", "33");
        setInput("String", "Entity 3");
        setInputTags("Strings", "e3", "e30");
        setSelectOption("TestEnum", "Two");
        setDropdownOptions("Enums", "One", "Two");

        Element embedded = findByLabel("Embedded", ".card");
        unhide(embedded, "Create");
        setInput(embedded, "EmbeddedInt", "300");
        setInput(embedded, "EmbeddedString", "Three hundreds");

        Element embeddeds = findByLabel("Embeddeds", ".card");
        clearTags(embeddeds);
        unhide(embeddeds, "Show embedded");
        setInput(embeddeds, "EmbeddedInt", "33");
        setInput(embeddeds, "EmbeddedString", "Embedded 33");
        embeddeds.find("button", "Add").includeHidden().one().click();
        checkTags(embeddeds, jsonToStrings(NEW_ROW.get(9)));

        Element relation = findByLabel("Relation", ".card");
        clearTags(relation);
        unhide(relation, "Show table");
        relation.find("button", "Choose").includeHidden().many(2).get(0).click();
        checkTags(relation, NEW_ROW.get(10));

        Element relations = findByLabel("Relations", ".card");
        clearTags(relations);
        unhide(relations, "Show table");
        relations.find("button", "Add").includeHidden().many(2)
                .forEach(Element::click);
        checkTags(relations, jsonToStrings(NEW_ROW.get(11)));

        setInput("ParentInt", "333");
        setInput("ParentString", "Thirty");
    }

    private void checkSpan(String label, String expectedValue) {
        String actualValue = findByLabel(label, "span").text();
        assertThat(actualValue.trim()).isEqualTo(expectedValue);
    }

    /* The single embedded object is rendered by the span as JSON,
       but the CSV holds only its title field (embeddedInt) -
       compare the parsed JSON against the fixture pattern instead */
    private void checkEmbeddedSpan(String embeddedInt) {
        String actualValue = findByLabel("Embedded", "span").text();
        assertThatCode(() -> {
            JsonNode node = objectMapper.readTree(actualValue);
            assertThat(node.get("embeddedInt").asText()).isEqualTo(embeddedInt);
            assertThat(node.get("embeddedString").asText()).isEqualTo(embeddedStringFor(embeddedInt));
        }).doesNotThrowAnyException();
    }

    private String embeddedStringFor(String embeddedInt) {
        return "".equals(embeddedInt) ? "" : "Embedded " + embeddedInt;
    }

    private void checkInput(String label, String expectedValue) {
        String actualValue = findByLabel(label, "input").value();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    private void checkInput(Element container, String label, String expectedValue) {
        String actualValue = findByLabel(container, label, "input").value();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    private void checkCheckbox(String label, String expectedValue) {
        WebElement checkbox = (WebElement) findByLabel(label, "input");
        assertThat(checkbox.isSelected()).isEqualTo(Boolean.parseBoolean(expectedValue));
    }

    private void checkInputTags(String label, String... values) {
        Element container = findByLabel(label, ".taginput");
        List<Element> tags = container.find("span.tag").includeHidden().many(values.length);
        assertThat(tags).extracting(Element::text).containsExactly(values);
    }

    private void checkSelect(String label, String value) {
        Select selectElement = select(label);
        if ("".equals(value)) {
            assertThat(selectElement.getAllSelectedOptions()).isEmpty();
            return;
        }
        assertThat(selectElement.getAllSelectedOptions())
                .extracting(option -> option.getAttribute("value"))
                .containsExactly(value);
    }

    private void checkDropdown(String label, String value) {
        Element select = findByLabel(label, "button");
        if ("".equals(value)) {
            assertThat(select.text()).isEqualTo("Please select elements");
            return;
        }
        assertThat(select.text()).isEqualTo(value);
    }

    private void checkTags(Element container, String... values) {
        if (values.length == 1 && values[0].equals("")) {
            values = new String[0];
        }
        List<Element> tags = container.find("span.tag").includeHidden().many(values.length);
        for (int i = 0; i < tags.size(); i++) {
            String tagText = tags.get(i).text();
            String value = values[i];
            assertThatCode(() -> {
                assertThat(objectMapper.readTree(tagText)).isEqualTo(objectMapper.readTree(value));
            }).doesNotThrowAnyException();
        }
    }

    private void setCheckbox(String label, boolean value) {
        WebElement input = (WebElement) findByLabel(label, "input");
        if (input.isSelected() != value) {
            findByLabel(label, "label").click();
        }
    }

    private void setInput(String label, String value) {
        Element input = findByLabel(label, "input");
        input.clear();
        input.clear();
        input.sendKeys(value);
    }

    private void setInput(Element container, String label, String value) {
        Element input = findByLabel(container, label, "input");
        input.clear();
        input.clear();
        input.sendKeys(value);
    }

    private void setInputTags(String label, String... values) {
        Element container = findByLabel(label, ".taginput");
        container.find(".tag").includeHidden().min(0).stream()
                .map(tag -> tag.find(".delete").includeHidden().one())
                .forEach(Element::click);
        Element input = container.find("input").includeHidden().one();
        Stream.of(values).forEach(v -> input.sendKeys(v + Keys.ENTER));
    }

    private void setSelectOption(String label, String value) {
        select(label).selectByVisibleText(value);
    }

    private void setDropdownOptions(String label, String... values) {
        Element dropdown = findByLabel(label, ".dropdown");
        Element dropdownButton = dropdown.find("button").includeHidden().one();
        dropdownButton.click();
        dropdown.find("a.is-active").includeHidden().min(0)
                .forEach(Element::click);
        Stream.of(values)
                .forEach(v -> dropdown.find("a", v).includeHidden().one().click());
        dropdownButton.click();
    }

    private void clearTags(Element container) {
        container.find(".tags").min(0)
                .forEach(t -> t.find("a").includeHidden().one().click());
    }

    private void unhide(Element container, String hiderTextIfHided) {
        container.find("a").includeHidden().min(0).stream()
                .filter(e -> hiderTextIfHided.equals(e.text()))
                .findFirst()
                .ifPresent(Element::click);
    }

    private Element findByLabel(String label, String selector) {
        return findByLabel(this, label, selector);
    }

    private Element findByLabel(Finder container, String label, String selector) {
        return container.findWithLabel(label).bySelector(selector).includeHidden().one();
    }

    private Select select(String label) {
        Element element = findByLabel(label, "select");
        return new Select((WebElement) element);
    }

    private String[] jsonToStrings(String json) {
        if ("".equals(json)) {
            return new String[0];
        }
        JsonNode array;
        try {
            array = objectMapper.readTree(json);
        } catch (IOException e) {
            fail("Failed due to IOException", e);
            throw new AssertionError();
        }
        return StreamSupport.stream(array.spliterator(), false)
                .map(node -> {
                    if (node.isTextual()) {
                        return node.textValue();
                    }
                    return node.toString();
                })
                .toArray(String[]::new);
    }

    private static List<List<String>> readCsv(String filename) {
        List<List<String>> rows = new ArrayList<>();
        assertThatCode(() -> {
            try (InputStream inputStream = AdminPanelTest.class.getResourceAsStream(filename);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    CsvListReader parser = new CsvListReader(bufferedReader, CsvPreference.EXCEL_PREFERENCE)) {

                List<String> row;
                while ((row = parser.read()) != null) {
                    row = row.stream()
                            .map(cell -> cell == null ? "" : cell)
                            .collect(Collectors.toList());
                    rows.add(row);
                }
            }
        }).doesNotThrowAnyException();
        return rows;
    }
}
