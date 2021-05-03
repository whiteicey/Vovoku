package info.skyblond.vovoku.worker

import info.skyblond.vovoku.commons.ModelTrainingParameter
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.learning.config.IUpdater
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions

fun getNeuralNetworkConfig(
    seed: Long, updater: IUpdater, l2: Double,
    hiddenLayerSize: Int, inputSize: Int, outputSize: Int
): MultiLayerConfiguration {
    return NeuralNetConfiguration.Builder()
        .seed(seed)
        .updater(updater)
        .l2(l2)
        .list()
        .layer(
            DenseLayer.Builder()
                .nIn(inputSize)
                .nOut(hiddenLayerSize)
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .build()
        )
        .layer(
            OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                .nIn(hiddenLayerSize)
                .nOut(outputSize)
                .activation(Activation.SOFTMAX)
                .weightInit(WeightInit.XAVIER)
                .build()
        )
        .build()
}

fun parseUpdater(updateType: ModelTrainingParameter.Updater, parameters: List<Double>): IUpdater {
    return when (updateType) {
        ModelTrainingParameter.Updater.Adam -> Adam(
            parameters[0], parameters[1],
            parameters[2], parameters[3]
        )
        ModelTrainingParameter.Updater.Nesterovs -> Nesterovs(
            parameters[0], parameters[1]
        )
    }
}