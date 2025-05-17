import { User } from "../global";

export default eventHandler(async (event) => {
  if (!event.path.startsWith("/api") || event.path.startsWith("/api/_auth")) {
    return;
  }

  const session = (await requireUserSession(event)).user as User;

  if (session.role == "admin") {
    return;
  } else if (
    session.role == "journalist" &&
    event.path.startsWith("/api/news")
  ) {
    return;
  }

  throw createError({
    statusCode: 401,
    statusMessage: "Unauthorized",
  });
});
