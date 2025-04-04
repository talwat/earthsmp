import { readFile } from "fs/promises";

export default defineEventHandler(async (event) => {
  let roles: Record<string, string> = JSON.parse(
    await readFile("./roles.json", "utf8"),
  );
  let user = event.context.params!.user;

  if (user in roles) {
    return roles[user];
  } else {
    return "unknown";
  }
});
