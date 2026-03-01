package com.example.tpjakarta.ai;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.tribuo.*;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.dtree.CARTClassificationTrainer;
import org.tribuo.data.csv.CSVDataSource;
import org.tribuo.data.columnar.FieldProcessor;
import org.tribuo.data.columnar.RowProcessor;
import org.tribuo.data.columnar.processors.field.IdentityProcessor;
import org.tribuo.data.columnar.processors.response.FieldResponseProcessor;
import org.tribuo.evaluation.TrainTestSplitter;
import org.tribuo.impl.ArrayExample;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class PluiePredictionTests {
    private static final String fileName = "livraison_retards_dataset.csv";
    private static final Path input = Paths.get("src", "main", "resources", fileName);

    private static LabelFactory labelFactory;
    private static LinkedHashMap<String, FieldProcessor> fieldProcessors;
    private static RowProcessor<Label> rowProcessor;
    private static CSVDataSource<Label> dataSource;
    private static Dataset<Label> train;
    private static Dataset<Label> test;
    private static Model<Label> model;

    @BeforeAll
    public static void setUp() {
        labelFactory = new LabelFactory();
        fieldProcessors = new LinkedHashMap<>();

        // On veut prédire 'pluie' à partir de 'jour_semaine' et 'retard'
        fieldProcessors.put("jour_semaine", new IdentityProcessor("jour_semaine"));
        fieldProcessors.put("retard", new IdentityProcessor("retard"));

        FieldResponseProcessor<Label> responseProcessor = new FieldResponseProcessor<>("pluie", "non", labelFactory);

        rowProcessor = new RowProcessor<>(responseProcessor, fieldProcessors);
    }

    @Test
    @Order(1)
    void loadDatasets() throws IOException {
        dataSource = new CSVDataSource<>(
                input,
                rowProcessor,
                true // skip header
        );
        assertNotNull(dataSource);
    }

    @Test
    @Order(2)
    void splitTrainTest() {
        var splitter = new TrainTestSplitter<>(dataSource, 0.8, 42L);
        train = new MutableDataset<>(splitter.getTrain());
        test = new MutableDataset<>(splitter.getTest());
    }

    @Test
    @Order(3)
    void training() {
        // Utilisation de CARTTrainer pour l'arbre de décision
        var trainer = new CARTClassificationTrainer();
        model = trainer.train(train);
    }

    @Test
    @Order(4)
    void evaluator() {
        var evaluator = new LabelEvaluator();
        LabelEvaluation evaluation = evaluator.evaluate(model, test);
        System.out.println("Résultats de l'évaluation (Prédiction Pluie) :");
        System.out.println(evaluation.toString());
        System.out.println("Confusion Matrix:");
        System.out.println(evaluation.getConfusionMatrix().toString());
    }

    @Test
    @Order(5)
    void predictor() {
        Example<Label> example = new ArrayExample<>(new Label("non"));
        example.add(new Feature("jour_semaine@vendredi", 1.0));
        example.add(new Feature("retard@oui", 1.0));

        Prediction<Label> prediction = model.predict(example);
        System.out.println("🎯 Prédiction Pluie pour Vendredi avec Retard : " + prediction.getOutput());
    }
}
