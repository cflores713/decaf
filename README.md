# decaf
![Image of SS](https://github.com/cflores713/decaf/blob/master/newSS.png)

A collaborative endeavor to create a small code editor for the Java programming language; hence the name *decaf*.

Team Member | GitHub Link
------------ | -------------
:octocat: Carlos Flores | https://github.com/cflores713
:squirrel: Shaun Byrnes | https://github.com/pbyrnes01
:cat: Caitie Hall | https://github.com/chall18
:wolf: Katherine Kin | https://github.com/katherinekin
:bowtie: Christopher Moran | https://github.com/chrismoran95

# Design Document

Editor screen
- New project (blank screen)
- File button (dropdown)
  - New (creates new project)
  - Open (opens dialog to select project)
  - Save
    - If project does not exist, Save dialog
    - Else save project
  - Exit (opens dialog to exit project)
    - If changes saved already, exit
    - Else, Exit dialog
- Compile (Build program)
- Execute (Compile if there are changes since last compile

	If last build exists:
		If no changes since last build, run
		else open compile dialog1
	Else:
		Compile dialog2)

Compile dialog1
- Message: Would you like to compile the latest changes?
- Yes button
- No button
- Cancel button

Open dialog
- User can browse to project
- Only show .java files
- Open button (opens selected project)
- Cancel button (x, closes the dialog)

Save dialog
- User can browse to folder
- User can name project

Exit dialog
- Message: Save changes?
- Save button
- Don't Save button
- Cancel button