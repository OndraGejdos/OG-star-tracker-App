package og.ogstartracker.network

sealed class Status {
	data object NotStarted : Status()
	data object Success : Status()
	data object Error : Status()
	data object Loading : Status()
}