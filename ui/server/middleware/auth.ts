import { getServerSession } from "#auth";
import { readFile } from "fs/promises";

export default eventHandler(async (event) => {
  const session = await getServerSession(event);

  console.log(event.path)

  if (event.path.startsWith("/api/auth") || !event.path.startsWith("/api")) {
    return;
  }

  let roles = readFile("./roles.json");

  let name = session?.user?.name;
  if (session && name != null && name in roles) {
    let role = roles[name as keyof typeof roles];

    if (role == "admin") {
      return;
    } else if (role == "journalist" && event.path.startsWith("/api/news")) {
      return;
    }
  }

  throw createError({
    statusCode: 401,
    statusMessage: "Unauthorized",
  });
});
