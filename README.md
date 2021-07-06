# About

WORK-IN-PROGRESS

JNovelist is a text management system integrated with **Git**.

# Features
It allows the user to organize the text as well as accompanying notes.

<b>Notes Categories</b>

- characters
- places
- artifacts
- random
- pictures

A novel consists of parts and parts.

# Git Integration

Every significant change to the text results in a **local commit**. 
It is done automatically in a background thread.

When the user chooses to **push** local changes they are sent to the remote server (origin).
Local commits are **squashed** before getting pushed.

If incoming changes are in **conflict** with the outgoing commit the user 
is offered to:
- save the project to a temporary local file (or Google Docs?),
- **hard-reset** the local repository,
- **pull** the changes from the remote,
- hand-pick necessary changes from the temporary file,
- **push** again.

There is no conflict resolution UI (diff) in JNovelist. 
As an alternative the user is offered to resolve the conflict in an external program.

So, Git integration is simple and its purpose is maximum safety of text created by one user.
