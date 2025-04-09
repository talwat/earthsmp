import { getServerSession } from "#auth";

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

  // Only handle API routes, since non API routes are
  // handled by the client middleware.
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

  let user = session.user as User | undefined;

  if (user == undefined) {
    throw error;
  }

  if (user.role == "admin") {
    return;
  } else if (user.role == "journalist" && event.path.startsWith("/api/news")) {
    return;
  }

  throw error;
});
