package software.plusminus.admin.converter.html.field;

import org.springframework.stereotype.Component;
import software.plusminus.admin.model.html.Video;
import software.plusminus.type.model.field.VideoField;

@Component
public class VideoConverter implements ElementConverter<VideoField, Video> {

    @Override
    public Class<VideoField> fieldType() {
        return VideoField.class;
    }

    @Override
    public Video convert(VideoField field) {
        Video video = new Video();
        video.setName(field.getName());
        return video;
    }
}
