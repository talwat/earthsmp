import { getServerSession } from "#auth";
import { readFile } from "fs/promises";

export interface User {
  name: string;
  role: string;
}

export default eventHandler(async (event) => {
  const session = await getServerSession(event);
  const error = createError({
    statusCode: 401,
    statusMessage: "Unauthorized",
  });

  if (
    event.path.startsWith("/api/auth") ||
    event.path.startsWith("/api/role") ||
    !event.path.startsWith("/api")
  ) {
    return;
  }

  if (session == null) {
    throw error;
  }

  let user: User = session.user as User;

  if (user == null) {
    throw error;
  }

  if (user.role == "admin") {
    return;
  } else if (
    user.role == "journalist" &&
    (event.path.startsWith("/api/news") || event.path.startsWith("/news"))
  ) {
    return;
  }

  throw createError({
    statusCode: 401,
    statusMessage: "Unauthorized",
  });
});
