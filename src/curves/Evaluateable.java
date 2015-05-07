package curves;

public interface Evaluateable {
	double rombergEvaluation(int piece, double lower, double higher, int n);
	double simpsonEvaluation(int piece, double lower, double higher, int n);
}
