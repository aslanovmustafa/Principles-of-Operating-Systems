Assignment	1
The	task	is	to	improve	the	default	implementation	of	the	readers/writer	problem.	Due	
to	larger	number	of	reader	threads,	writer	has	lower	chance	to	acquire	the	DB	lock.	
Therefore,	you	must come	up	with	a	solution	such	that	whenever	writer	has	an	
intention	of accessing	the	DB,	readers	trying	to	access	it	should	wait	for	the	writer	to	
complete	its	work.
Instead	of	DB, your	solution	should	use files.	Furthermore,	instead	of	one	file,	there	are	
going	to	be	3 copies	(replicas)	of	the	same	file,	so	that	whenever	readers	try	to	access	
the	file,	solution	should	forward	them	to	one	of	the	copies	(consider	balancing,	having	
minimal	difference between	the	numbers of	readers	accessing	each	file).
Here	is	the	summary:
• Indefinite	amount	of	reader	threads	spawning	at	random	times for	reading	just	
once
• A	single	writer	thread	trying	to	access	the	file at	random	periods	(use	random	
sleep value	for	simulation)
• Three	copies	of	the	same	text	file
• Readers	can	access	any	copy of	the	file
• Number of	readers	of	the	copies	should	be	approximately	same
• Readers	and	writer	cannot	access	the	files	at	the	same	time
• Writer	locks	and	modifies all	of	the	files	simultaneously
• Each	read/write	action	should	follow	with	a	log	info (written	to	the standard	
output) indicating	current	state	of	the	system	(number	of	readers/writers
accessing	each	file,	content of	the	files)
You	are	free	to	use	one	of	the	following	languages	to	implement	the	task:	C/C++,	Java,	
Python.
Your	solution	should	be	thread	safe,	implemented	in	a	proper	way using	mutexes,	
semaphores,	monitors or	conditional	variables.
