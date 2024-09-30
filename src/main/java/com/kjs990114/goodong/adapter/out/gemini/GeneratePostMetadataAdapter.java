package com.kjs990114.goodong.adapter.out.gemini;

import com.kjs990114.goodong.application.port.out.ai.GeneratePostMetadataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GeneratePostMetadataAdapter implements GeneratePostMetadataPort {

    private final VertexAiGeminiChatClient geminiClient;

    @Override
    public PostAttributes generatePost(MultipartFile filePng) throws IOException {
        byte[] imgBytes = filePng.getBytes();

        UserMessage multiModalUserMessage = new UserMessage("""
                You must give me three tagEntities, each composed of a noun.
                Each tag is separated by a comma, and the words within a tag may contain spaces,
                but there should be no spaces between the tagEntities, only commas.
                Each tag must be carefully selected, and one single tag should be enough to fully describe the entire 3D modelEntity.
                You are only allowed to give me three tagEntities in total.
                Ensure that there are no spaces between the commas.
                and the description must be written as if you are the creator of the modelEntity and it it a statement, not a words,
                describing it from that perspective at the time of its creation.
                The title must not contain commas and must be a single word that must not overlap with the tagEntities and must represent the whole modelEntities of Image in the best possible way.
                And you must not contain any line break words in title , description, tagEntities
                Additionally, you must provide the description of the modelEntity in the format `title|description|tagEntities`,
                """,
                List.of(new Media(MimeTypeUtils.IMAGE_PNG, imgBytes)));

        ChatResponse response = geminiClient.call(
                new Prompt(List.of(multiModalUserMessage))
        );

        List<String> elements = Arrays.asList(response.getResult().getOutput().getContent().split("\\|"));

        String title = elements.get(0);
        String content = elements.get(1);
        List<String> tags = Arrays.stream(elements.get(2).split(",")).toList();

        return new PostAttributes(title,content,tags);

    }
}
