import { getServerSession } from "#auth";

export default eventHandler(async (event) => {
  const session = await getServerSession(event);

  if (
    !session &&
    !event.path.startsWith("/api/auth") &&
    event.path.startsWith("/api")
  ) {
    throw createError({
      statusCode: 401,
      statusMessage: "Server Unauthorized",
    });
  }
});
