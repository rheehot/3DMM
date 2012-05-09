package model;

import java.nio.IntBuffer;

import pca.PCA;
import pca.PCA_SVD;

public class MorphableModel {

	public final static int PCA_DIMENSION = 5;
	private PCA vertices;
	private PCA colors;
	private IntBuffer faceIndices;

	public MorphableModel() {
		faceIndices = null;
		vertices = new PCA_SVD();
		colors = new PCA_SVD();
	}

	public void addModel(Model model) {
		if(faceIndices == null)
			faceIndices = model.getFaceIndices();

		if(vertices == null)
			vertices = new PCA_SVD();
		if(colors == null)
			colors = new PCA_SVD();

		vertices.addSample(model.getVerticesMatrix());
		colors.addSample(model.getColorMatrix());
	}

	public Model getModel(int index) {
		return new Model(vertices.getSample(index), colors.getSample(index), faceIndices);
	}

	public Model getReducedModel(int index) {
		ensurePCA();
		return new Model(vertices.getFeatureSample(index), colors.getFeatureSample(index), faceIndices);
	}

	public Model getModel(ModelParameter param) {
		ensurePCA();

		return new Model(vertices.sampleToSampleSpace(param.getVerticesWeight()),
									   colors.sampleToSampleSpace(param.getColorWeight()),
										 faceIndices);
	}

	public void updateModel(Model model, ModelParameter param) {
		ensurePCA();
		model.setVerticesMatrix(vertices.sampleToSampleSpace(param.getVerticesWeight()));
		model.setColorMatrix(colors.sampleToSampleSpace(param.getColorWeight()));
	}

	public int getSize() {
		assert(vertices.getSampleNumber() == colors.getSampleNumber());
		return vertices.getSampleNumber();
	}

	public int getReducedSize() {
		ensurePCA();
		assert(vertices.getNumComponents() == colors.getNumComponents());
		return vertices.getNumComponents();
	}

	public Model getAverage() {
		return new Model(vertices.getMean(), colors.getMean(), faceIndices);
	}

	public void compute(int dimension) {
		if(!vertices.computationDone())
			vertices.computePCA(dimension);

		if(!colors.computationDone())
			colors.computePCA(dimension);
	}

	@Override
	public String toString() {
		if(!vertices.computationDone())
			return "Morphable Model (unlocked)";
		else
			return "Morphable Model (locked): " + vertices.getNumComponents() + " dimensions.";
	}

	private void ensurePCA() {
		compute(PCA_DIMENSION);
	}
}
