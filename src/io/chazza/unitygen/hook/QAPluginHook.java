package io.chazza.unitygen.hook;

import be.maximvdw.qaplugin.api.AIModule;
import be.maximvdw.qaplugin.api.AIQuestionEvent;
import be.maximvdw.qaplugin.api.QAPluginAPI;
import be.maximvdw.qaplugin.api.ai.Intent;
import be.maximvdw.qaplugin.api.ai.IntentResponse;
import be.maximvdw.qaplugin.api.ai.IntentTemplate;
import io.chazza.unitygen.UnityGenAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QAPluginHook extends AIModule {
    public QAPluginHook() {
        super("UnityGen", "Chazmondo", "Implement UnityGen into QAPlugin!");

        Intent question = new Intent("UnityGen")
                // Start with some full sentences
                .addTemplate("how many generators have I placed?")
                .addTemplate("how many generators have I put down?")
                .addTemplate("how many generators have I created?")
                .addTemplate("how many generator have I created?")
                // These sentences have a variable inside
                .addTemplate(new IntentTemplate()
                        // You can see the parts like chat in minecraft
                        // It is also in parts (some parts have hover, click action ,...)
                        .addPart("how many generators did ")
                        .addPart(new IntentTemplate.TemplatePart("Maximvdw") // Maximvdw as an example
                                .withAlias("player") // Parameter name
                                .withMeta("@sys.any")) // Entity type https://docs.api.ai/docs/concept-entities#section-system-entities
                        .addPart(" place?"))
                .addTemplate(new IntentTemplate()
                        .addPart("how many generators did ")
                        .addPart(new IntentTemplate.TemplatePart("Maximvdw")
                                .withAlias("player")
                                .withMeta("@sys.any"))
                        .addPart(" create?"))
                .addTemplate(new IntentTemplate()
                        .addPart("how many generators did ")
                        .addPart(new IntentTemplate.TemplatePart("Maximvdw")
                                .withAlias("player")
                                .withMeta("@sys.any"))
                        .addPart(" make?"))
                .addTemplate(new IntentTemplate()
                        .addPart("how many generators are made by ")
                        .addPart(new IntentTemplate.TemplatePart("Maximvdw")
                                .withAlias("player")
                                .withMeta("@sys.any"))
                        .addPart(" ?"))
                // Here we define what our response will return
                .addResponse(new IntentResponse()
                        // Set this class as the action
                        .withAction(this)
                        // This is the OPTIONAL parameter we are going to extract
                        .addParameter(new IntentResponse.ResponseParameter("player", "$player")));

        // Error responses
        addErrorResponse("other-no-generators", "That player has no generators!");
        addErrorResponse("other-no-generators", "That player does not seem to have a generator!");
        // Success responses depending on if you are asking the generators of you or another player
        addResponse("me-generators", "You have ${amount} generator(s)!");
        addResponse("me-generators", "You seem to have ${amount} generator(s)!");
        addResponse("other-generators", "${player} has ${amount} generator(s)!");
        addResponse("other-generators", "${player} seems to have ${amount} generator(s)!");

            // Upload the intents
            if (QAPluginAPI.findIntentByName(question.getName()) == null) { // Only upload if not exist
                if (!QAPluginAPI.uploadIntent(question)) { // Returns false when upload failed
                    warning("Unable to upload intent!");
                }
            }

    }

    public String getResponse(AIQuestionEvent event) {
        Player player = event.getPlayer();

        Map<String, String> params = event.getParameters();

        OfflinePlayer otherPlayer = null;
        // Check if the user gave a player name to the question
        if (params.containsKey("player")) {
            // Player specified
            // It can be "blah blah blah" so lets check if its a real player
            otherPlayer = Bukkit.getOfflinePlayer(params.get("player"));
        }

        // If the player is not null, get the generators of that player
        if (otherPlayer != null) {
            List<String> generators = UnityGenAPI.getPlayerGens(otherPlayer.getUniqueId());
            if (generators == null || generators.size() == 0) {
                // Error, has no generators
                return getRandomErrorResponse("other-no-generators", new HashMap<String, String>(), player);
            } else {
                // These are some placeholders that will replace
                // ${amount} , ${player} in the return response
                Map<String, String> placeholders = new HashMap<String, String>();
                placeholders.put("amount", String.valueOf(generators.size()));
                placeholders.put("player", otherPlayer.getName());
                return getRandomResponse("other-generators", placeholders, player);
            }
        } else {
            List<String> generators = UnityGenAPI.getPlayerGens(player.getUniqueId());
            // These are some placeholders that will replace
            // ${amount} , ${player} in the return response
            Map<String, String> placeholders = new HashMap<String, String>();
            placeholders.put("amount", String.valueOf(generators.size()));
            return getRandomResponse("me-generators", placeholders, player);
        }
    }
}