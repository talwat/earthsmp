import { readFile } from "fs/promises";

export default defineOAuthGitHubEventHandler({
  async onSuccess(event, { user }) {
    let roles: Record<string, string> = JSON.parse(
      await readFile("./roles.json", "utf8"),
    );

    let role = roles[user.login];

    await setUserSession(event, {
      user: {
        name: user.login,
        role: role,
      },
    });
    return sendRedirect(event, "/");
  },
});
