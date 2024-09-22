package com.kjs990114.goodong.application.post;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final VertexAiGeminiChatClient geminiClient;

    public List<String> getDescription(MultipartFile png_file) throws IOException {
        byte[] imgBytes = png_file.getBytes();

        UserMessage multiModalUserMessage = new UserMessage("""
                You must give me three tags, each composed of a noun.
                Each tag is separated by a comma, and the words within a tag may contain spaces,
                but there should be no spaces between the tags, only commas.
                Each tag must be carefully selected, and one single tag should be enough to fully describe the entire 3D model.
                You are only allowed to give me three tags in total.
                Ensure that there are no spaces between the commas.
                and the description must be written as if you are the creator of the model and it it a statement, not a words,
                describing it from that perspective at the time of its creation.
                The title must not contain commas and must be a single word that must not overlap with the tags and must represent the whole models of Image in the best possible way.
                And you must not contain any line break words in title , description, tags
                Additionally, you must provide the description of the model in the format `title|description|tags`,
                """,
                List.of(new Media(MimeTypeUtils.IMAGE_PNG, imgBytes)));

        ChatResponse response = geminiClient.call(
                new Prompt(List.of(multiModalUserMessage))
        );

        return Arrays.asList(response.getResult().getOutput().getContent().split("\\|"));


    }

}

