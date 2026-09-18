package eu.dunderburken;

enum FeedbackLevel {
	VERBOSE,
	NORMAL,
	CONCISE,
	SILENT,
	;

	static FeedbackLevel parse(String feedback) {
		if (feedback == null) {
			return FeedbackLevel.NORMAL;
		}

		return FeedbackLevel.valueOf(feedback.toUpperCase());
	}
}
