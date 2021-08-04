package voruti.shlinkdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShlinkCreatorTest {

    private static ShlinkCreator shlinkCreator;

    private SlashCommandEvent slashCommandEvent;


    @BeforeAll
    static void beforeAll() {
        JDA jda = mock(JDA.class);
        CommandListUpdateAction commandListUpdateAction = mock(CommandListUpdateAction.class);
        when(jda.updateCommands()).thenReturn(commandListUpdateAction);
        when(commandListUpdateAction.addCommands(any(CommandData.class))).thenReturn(commandListUpdateAction);
        shlinkCreator = new ShlinkCreator(jda, 5L, "http://example.com", "example");
    }

    @BeforeEach
    void setUp() {
        slashCommandEvent = mock(SlashCommandEvent.class);
    }


    @Test
    void onSlashCommand_RandomCommand() {
        // arrange:
        when(slashCommandEvent.getName()).thenReturn("foo");

        ReplyAction replyAction = mock(ReplyAction.class);
        when(slashCommandEvent.reply(any(String.class))).thenReturn(replyAction);
        when(replyAction.setEphemeral(true)).thenReturn(replyAction);

        // act:
        shlinkCreator.onSlashCommand(slashCommandEvent);

        // assert:
        verify(slashCommandEvent).reply("I can't handle that command right now :(");
    }

    @Test
    void onSlashCommand_ShlinkCommand() {
        // arrange:
        when(slashCommandEvent.getName()).thenReturn("shlink");

        ReplyAction replyAction = mock(ReplyAction.class);
        when(slashCommandEvent.deferReply()).thenReturn(replyAction);
        OptionMapping optionMapping = mock(OptionMapping.class);
        when(slashCommandEvent.getOption("long_url")).thenReturn(optionMapping);
        when(optionMapping.getAsString()).thenReturn("http://example.com");
        InteractionHook interactionHook = mock(InteractionHook.class);
        when(slashCommandEvent.getHook()).thenReturn(interactionHook);
        @SuppressWarnings("unchecked")
        WebhookMessageAction<Message> webhookMessageAction = (WebhookMessageAction<Message>) mock(WebhookMessageAction.class);
        when(interactionHook.sendMessage(any(String.class))).thenReturn(webhookMessageAction);

        // act:
        shlinkCreator.onSlashCommand(slashCommandEvent);

        // assert:
        verify(slashCommandEvent, never()).reply("I can't handle that command right now :(");
        verify(interactionHook, times(1)).sendMessage(any(String.class));
    }
}
