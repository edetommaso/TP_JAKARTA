package com.example.tpjakarta.blockchain;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class BlockchainGui extends Application {

    private Blockchain blockchain;
    private VBox cardsContainer;
    private ScrollPane scrollPane;
    private Label statusLabel;
    private Circle statusIndicator;

    @Override
    public void start(Stage primaryStage) {
        this.blockchain = new Blockchain();

        primaryStage.setTitle("Blockchain Tracking - Logistique & Billetterie");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #121212;");

        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        Label title = new Label("BLOCKCHAIN TRACKING");
        title.setStyle("-fx-text-fill: #00E676; -fx-font-size: 24px; -fx-font-weight: bold;");
        header.getChildren().add(title);
        root.setTop(header);

        cardsContainer = new VBox(20);
        cardsContainer.setPadding(new Insets(20));
        cardsContainer.setAlignment(Pos.TOP_CENTER);

        scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setCenter(scrollPane);

        HBox bottomBar = new HBox(20);
        bottomBar.setPadding(new Insets(20));
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #1E1E1E;");

        Button addBlockBtn = new Button("Minner Nouveau Bloc");
        addBlockBtn.setStyle("-fx-background-color: #00E676; -fx-text-fill: black; -fx-font-weight: bold;");
        addBlockBtn.setOnAction(e -> showAddBlockDialog());

        Button validateBtn = new Button("Vérifier Intégrité");
        validateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        validateBtn.setOnAction(e -> validateChain());

        Button scenarioBtn = new Button("🎬 Scénario Billetterie");
        scenarioBtn.setStyle("-fx-background-color: #BB86FC; -fx-text-fill: black; -fx-font-weight: bold;");
        scenarioBtn.setOnAction(e -> runTicketingScenario());

        statusIndicator = new Circle(8, Color.GRAY);
        statusLabel = new Label("Système Prêt");
        statusLabel.setStyle("-fx-text-fill: white;");

        bottomBar.getChildren().addAll(addBlockBtn, validateBtn, scenarioBtn, statusIndicator, statusLabel);
        root.setBottom(bottomBar);

        refreshUI();

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshUI() {
        cardsContainer.getChildren().clear();
        List<Block> chain = blockchain.getChain();
        for (int i = 0; i < chain.size(); i++) {
            boolean isValid = blockchain.isBlockValid(i);
            cardsContainer.getChildren().add(createBlockCard(chain.get(i), isValid));
        }
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private VBox createBlockCard(Block block, boolean isValid) {
        VBox card = new VBox(10);
        card.setMaxWidth(600);
        card.setPadding(new Insets(15));

        String borderColor = isValid ? "#00E676" : "#CF6679";
        String bgColor = isValid ? "#2C2C2C" : "#3D1C21";
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10; -fx-border-color: "
                + borderColor + "; -fx-border-radius: 10; -fx-border-width: 2;");

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label indexLabel = new Label("BLOCK #" + block.index);
        indexLabel.setStyle("-fx-text-fill: #BB86FC; -fx-font-weight: bold; -fx-font-size: 16px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String timeStr = block.timestamp.length() > 19 ? block.timestamp.substring(0, 19).replace("T", " ")
                : block.timestamp;
        Label timeLabel = new Label("🕒 " + timeStr);
        timeLabel.setStyle("-fx-text-fill: #AAA; -fx-font-size: 12px;");

        topRow.getChildren().addAll(indexLabel, spacer, timeLabel);

        Label statusIcon = new Label(isValid ? " ✅ INTEGRE" : " ❌ CORROMPU");
        statusIcon.setStyle("-fx-text-fill: " + borderColor + "; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label dataLabel = new Label("📋 " + block.data);
        dataLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        VBox details = new VBox(5);
        details.setPadding(new Insets(5, 10, 5, 10));
        if (block.owner != null) {
            details.setStyle(
                    "-fx-background-color: #1E1E1E; -fx-background-radius: 5; -fx-border-color: #03DAC6; -fx-border-width: 0.5;");
            Label ticketLabel = new Label("🎤 Artiste : " + block.artist.toUpperCase());
            ticketLabel.setStyle("-fx-text-fill: #03DAC6; -fx-font-weight: bold; -fx-font-size: 13px;");
            Label idLabel = new Label("🆔 ID Event : " + block.eventId);
            idLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 11px;");
            Label ownerLabel = new Label("👤 Possesseur : " + block.owner);
            ownerLabel.setStyle("-fx-text-fill: #E0E0E0; -fx-font-weight: bold; -fx-font-size: 12px;");
            details.getChildren().addAll(ticketLabel, idLabel, ownerLabel);
        }

        Label hashLabel = new Label("🔗 Hash : " + block.hash);
        hashLabel.setStyle("-fx-text-fill: #03DAC6; -fx-font-family: 'Courier New'; -fx-font-size: 11px;");

        Label prevHashLabel = new Label("⛓️ Prev : " + block.previousHash);
        prevHashLabel.setStyle("-fx-text-fill: #757575; -fx-font-family: 'Courier New'; -fx-font-size: 11px;");

        Button corruptBtn = new Button("⚠️ Corrompre");
        corruptBtn.setStyle("-fx-background-color: #CF6679; -fx-text-fill: white; -fx-font-size: 10px;");
        corruptBtn.setOnAction(e -> {
            block.data = "[FRAUDE] Donnée modifiée !";
            blockchain.exportAsJson("blockchain.json"); // Persister la fraude
            refreshUI();
            statusLabel.setText("Bloc #" + block.index + " corrompu !");
            statusIndicator.setFill(Color.RED);
        });

        card.getChildren().addAll(topRow, statusIcon, dataLabel, details, hashLabel, prevHashLabel, corruptBtn);
        return card;
    }

    private void runTicketingScenario() {
        statusLabel.setText("Simulation Scénario en cours...");
        statusIndicator.setFill(Color.BLUE);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    blockchain.addTicketBlock("Achat Ticket Concert", "EVT-2024", "The Weeknd", "ACTIF", "Eric");
                    refreshUI();
                });

                Thread.sleep(1000);
                Platform.runLater(() -> {
                    blockchain.addTicketBlock("Revente Ticket #1", "EVT-2024", "The Weeknd", "ACTIF", "Alice");
                    refreshUI();
                });

                Thread.sleep(1000);
                Platform.runLater(() -> {
                    blockchain.addTicketBlock("Revente Ticket #2", "EVT-2024", "The Weeknd", "ACTIF", "Bob");
                    refreshUI();
                });

                Thread.sleep(1000);
                Platform.runLater(() -> {
                    blockchain.addTicketBlock("Ticket Scanné à l'entrée", "EVT-2024", "The Weeknd", "UTILISE", "Bob");
                    refreshUI();
                    statusLabel.setText("Scénario Billetterie terminé !");
                    statusIndicator.setFill(Color.LIME);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAddBlockDialog() {
        Dialog<BlockData> dialog = new Dialog<>();
        dialog.setTitle("Minner un nouveau Bloc");
        dialog.setHeaderText("Saisie des détails du bloc (Logistique ou Billetterie)");

        ButtonType mineButtonType = new ButtonType("Minner", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(mineButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField dataField = new TextField();
        dataField.setPromptText("Description (ex: Vente billet)");
        TextField artistField = new TextField();
        artistField.setPromptText("Nom de l'artiste (Optionnel)");
        TextField ownerField = new TextField();
        ownerField.setPromptText("Possesseur (Optionnel)");
        TextField eventField = new TextField();
        eventField.setPromptText("ID Événement (Optionnel)");

        grid.add(new Label("Description:"), 0, 0);
        grid.add(dataField, 1, 0);
        grid.add(new Label("Artiste:"), 0, 1);
        grid.add(artistField, 1, 1);
        grid.add(new Label("Possesseur:"), 0, 2);
        grid.add(ownerField, 1, 2);
        grid.add(new Label("ID Event:"), 0, 3);
        grid.add(eventField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == mineButtonType) {
                return new BlockData(dataField.getText(), artistField.getText(), ownerField.getText(),
                        eventField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            statusLabel.setText("Minage en cours...");
            statusIndicator.setFill(Color.ORANGE);

            new Thread(() -> {
                try {
                    Thread.sleep(800);
                    Platform.runLater(() -> {
                        if (result.artist != null && !result.artist.isEmpty()) {
                            blockchain.addTicketBlock(result.data, result.eventId, result.artist, "ACTIF",
                                    result.owner);
                        } else {
                            blockchain.addBlock(result.data);
                        }
                        refreshUI();
                        statusLabel.setText("Bloc #" + (blockchain.getChain().size() - 1) + " miné et ajouté !");
                        statusIndicator.setFill(Color.LIME);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private static class BlockData {
        String data, artist, owner, eventId;

        BlockData(String d, String a, String o, String e) {
            this.data = d;
            this.artist = a;
            this.owner = o;
            this.eventId = e;
        }
    }

    private void validateChain() {
        boolean isValid = blockchain.isChainValid();
        if (isValid) {
            statusLabel.setText("La blockchain est parfaitement intègre.");
            statusIndicator.setFill(Color.LIME);
        } else {
            statusLabel.setText("ALERTE : Blockchain corrompue !");
            statusIndicator.setFill(Color.RED);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
