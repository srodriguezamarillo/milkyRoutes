---
description: Deliver a task end to end - branch from updated main, implement, verify, commit, push with approval, and open a PR
agent: build
---

Deliver this task end to end: $ARGUMENTS

Follow the rules in AGENTS.md at every step. Work through the steps in order and
stop at the first step that fails, reporting honestly what happened.

## 1. Inspect
- Read AGENTS.md first, then every file relevant to the task.
- Explain your approach briefly before making complex modifications.
- Do not modify application logic or unrelated files.

## 2. Prepare the branch
- Run `git status --short --branch`. Stop if the working tree is not clean.
- Switch to main, fetch, and update it fast-forward only:
  `git switch main && git fetch origin && git merge --ff-only origin/main`.
- Create a focused branch from the updated main:
  `git switch -c <type>/<slug>` where type is `feature`, `fix`, or `chore`.
- Never commit directly to main.

## 3. Implement
- Make minimal, focused changes that preserve existing contracts.
- Reuse existing APIs and components.
- Never replace custom algorithms with external solvers.

## 4. Tests
- Add regression tests for every bug fix and for new behavior worth locking in.
- Never weaken existing tests to achieve a successful build.

## 5. Verify
- Run the full verification with Java 21 in headless mode:
  `JAVA_HOME=<JDK 21 path> JAVA_TOOL_OPTIONS=-Djava.awt.headless=true mvn -B clean verify`
- Stop if it fails; fix and re-run until it passes, or stop and report the failure.
- Never claim success without real execution output.

## 6. Review and commit
- Inspect `git status --short` and `git diff` before committing.
- Stage only the files you intentionally changed.
- Commit in small atomic commits with concise English messages using
  Conventional Commits (`feat:`, `fix:`, `docs:`, `ci:`, `test:`, `chore:`).

## 7. Push and open the PR
- Ask the user for approval to push. Never push without it.
- After approval, push with `git push -u origin <branch>`.
- Create the PR with the GitHub CLI: `gh pr create --base main --title "<title>" --body "<body>"`
  with concise English sections: Summary, Changes, Testing (real results), Risks.
- Never merge the PR automatically. Never force-push, reset, clean, or delete branches.
- Report the PR URL and the actual verification results.
