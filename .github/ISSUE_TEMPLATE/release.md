---
name: Release checklist
about: Steps to ship an Android build to Google Play.
title: "Release <version> (versionCode <number>)"
labels: ''
assignees: ''

---

Copy this checklist into a new issue for each Google Play release. Delete anything that doesn't apply.

## Before the build

- [ ] Every PR for the release is merged into `develop`.
- [ ] `VERSION_CODE` and `VERSION_NAME` in `plugins/src/main/kotlin/Versions.kt` are bumped, and `VERSION_CODE` is higher than any build already uploaded to Play Console.
- [ ] `tools/manifest/gplay/release/aaptDump.txt` is regenerated to match. It embeds the exact versionCode (multiplied by 10 for the ABI suffix) and versionName, and the "Check manifest has not changed" CI job diffs against it verbatim.
- [ ] Add a changelog file under `fastlane/metadata/android/en-US/changelogs/` if the release needs one (named after the final versionCode).
- [ ] Anything the app links to is live: terms, privacy policy, support page, support email (send a test message).
- [ ] The Play review demo account signs in on a fresh device against production, including any identity-confirmation step. Keep its credentials and recovery key out of chat and issues.

## Build

- [ ] Open a `develop` → `main` PR with exactly one `pr-` label (e.g. `pr-misc`). Merge it with a **merge commit** (not squash or rebase).
- [ ] The "Create release App Bundle and APKs" workflow runs on the push to `main` (or trigger it with `workflow_dispatch`) and succeeds.
- [ ] Download the `elementx-app-gplay-bundle-unsigned` artifact.
- [ ] Sign the `.aab` with the upload key. Keep the keystore and passwords out of the repo, chat and issues.
- [ ] Confirm the signed bundle has the expected package name, versionCode and versionName.
- [ ] Tag the released commit `v<version>` and push only that tag (`git push origin <tag>`, never `--tags`).

## Test the build

- [ ] Upload to an internal testing track in Play Console and wait for processing.
- [ ] Install from the Play Store on a physical device.
- [ ] Sign in and out on a fresh install.
- [ ] Send and receive messages, including in an encrypted room.
- [ ] Safety features Play review expects still work: terms shown before signing in, reporting, blocking, and a reachable contact address.
- [ ] Push notifications, calls and anything else this release touched.
- [ ] Upgrade from the previous production version keeps the session and message history.
- [ ] If the release touches sign-in, registration, token or session handling, or anything depending on MAS, test it against the test-MAS stack first (a build pointed at the test homeserver and auth service, not production): sign in, invite-only registration, password recovery and verification emails, and sign out. Confirm the stack is up and matches production's MAS version and config before relying on it.

## Play Console

- [ ] Promote the build to the production track (or a staged rollout).
- [ ] "What's new" text is written.
- [ ] Store listing and screenshots meet current requirements.
- [ ] Data safety form, privacy policy URL, content rating and target audience answers are correct.
- [ ] App access instructions include demo credentials and how to reach each feature under review.
- [ ] Countries/regions and any region-specific restrictions are correct.
- [ ] Send for review.

## While in review

- [ ] Demo account and servers (homeserver, auth service) stay up.
- [ ] Watch the support mailbox and the Play Console inbox; reply to reports and Google's questions promptly.
- [ ] If rejected: fix, bump the versionCode, re-run this checklist from the top, and reply in Play Console.

## After approval

- [ ] Roll out to 100% when the staged rollout looks healthy (crash rate, reports).
- [ ] Publish a GitHub Release from the tag (optional; `--generate-notes` uses the `pr-` labels).
- [ ] Merge `main` back into `develop` if the branches diverged.
- [ ] Close this issue.
