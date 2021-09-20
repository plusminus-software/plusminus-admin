package software.plusminus.admin.converter.html.field;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.CarouselList;
import software.plusminus.admin.model.html.Upload;
import software.plusminus.type.model.field.ArrayField;
import software.plusminus.type.model.field.UrlField;

@Order(1)
@Component
public class ArrayUrlToCarouselListConverter implements ArrayElementConverter<UrlField, CarouselList> {

    @Override
    public Class<UrlField> arrayType() {
        return UrlField.class;
    }

    @Override
    public boolean supports(ArrayField field) {
        return ArrayElementConverter.super.supports(field)
                && field.getAnnotations().stream()
                .anyMatch(f -> f.getName().equals(
                        software.plusminus.admin.annotation.CarouselList.class.getSimpleName()));
    }

    @Override
    public CarouselList convertArray(ArrayField arrayField, UrlField urlField) {
        CarouselList carouselList = new CarouselList();
        carouselList.setName(arrayField.getName());
        Upload upload = new Upload();
        upload.setDragAndDrop(true);
        upload.setUrl("/api/products/%s/photos");
        carouselList.setUpload(upload);
        return carouselList;
    }
}
