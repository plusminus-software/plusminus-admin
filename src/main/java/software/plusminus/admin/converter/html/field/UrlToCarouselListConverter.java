package software.plusminus.admin.converter.html.field;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.CarouselList;
import software.plusminus.admin.model.html.Upload;
import software.plusminus.type.model.field.UrlField;

@Order(1)
@Component
public class UrlToCarouselListConverter implements ElementConverter<UrlField, CarouselList> {

    @Override
    public Class<UrlField> fieldType() {
        return UrlField.class;
    }

    @Override
    public boolean supports(UrlField field) {
        return ElementConverter.super.supports(field)
                && field.getAnnotations().stream()
                .anyMatch(f -> f.getName().equals(
                        software.plusminus.admin.annotation.CarouselList.class.getSimpleName()));
    }

    @Override
    public CarouselList convert(UrlField field) {
        CarouselList carouselList = new CarouselList();
        carouselList.setName(field.getName());
        Upload upload = new Upload();
        upload.setDragAndDrop(true);
        upload.setUrl("/api/products/%s/photos");
        carouselList.setUpload(upload);
        carouselList.setSingle(true);
        return carouselList;
    }
}
