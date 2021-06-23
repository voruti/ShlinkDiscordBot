package voruti.shlinkdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.entities.UserById;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShlinkCreatorTest {

    private static ShlinkCreator shlinkCreator;

    private MessageReceivedEvent event;


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
        event = mock(MessageReceivedEvent.class);
    }


    @Test
    void onMessageReceived_SelfMessage() {
        // arrange:
        User theBotItself = new UserById(5L);
        when(event.getAuthor()).thenReturn(theBotItself);

        // act:
        shlinkCreator.onMessageReceived(event);

        // assert:
        verify(event, never()).getMessage();
    }

    @Test
    void onMessageReceived_RandomMessage() {
        // arrange:
        User author = new UserById(10L);
        when(event.getAuthor()).thenReturn(author);

        Message message = new MessageBuilder().append("message should be ignored").build();
        when(event.getMessage()).thenReturn(message);
        MessageChannel channel = mock(MessageChannel.class);
        when(event.getChannel()).thenReturn(channel);

        // act:
        shlinkCreator.onMessageReceived(event);

        // assert:
        verify(event).getMessage();
        verify(event).getChannel();
        verify(channel, never()).sendMessage(any(CharSequence.class));
    }

    @Test
    void onMessageReceived_HelpMessage() {
        // arrange:
        User author = new UserById(10L);
        when(event.getAuthor()).thenReturn(author);

        Message message = new MessageBuilder().append("!help").build();
        when(event.getMessage()).thenReturn(message);
        MessageChannel channel = mock(MessageChannel.class);
        when(event.getChannel()).thenReturn(channel);

        when(channel.sendMessage(any(CharSequence.class))).thenReturn(mock(MessageAction.class));

        // act:
        shlinkCreator.onMessageReceived(event);

        // assert:
        verify(channel).sendMessage("!addShlink <long URL> [custom slug]\t:\tCreate a short link from <long URL> with optional [custom slug].");
    }

    @Test
    void onMessageReceived_AddShlinkMessage() {
        // arrange:
        User author = new UserById(10L);
        when(event.getAuthor()).thenReturn(author);

        Message message = new MessageBuilder().append("!addShlink example.com").build();
        when(event.getMessage()).thenReturn(message);
        MessageChannel channel = mock(MessageChannel.class);
        when(event.getChannel()).thenReturn(channel);

        when(channel.sendMessage(any(CharSequence.class))).thenReturn(mock(MessageAction.class));

        // act:
        shlinkCreator.onMessageReceived(event);

        // assert:
        verify(channel).sendMessage("Error with Shlink's response: \"null\"!");
    }
}
