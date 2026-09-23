package eu.dunderburken;

enum FeedbackLevel {
	VERBOSE,
	NORMAL,
	CONCISE,
	SILENT,
	;

	static String parse(String feedback) {
		FeedbackLevel level = NORMAL;
		if (feedback == null) {
			level = valueOf(feedback);
		}

		return level.name().toLowerCase();
	}

}
