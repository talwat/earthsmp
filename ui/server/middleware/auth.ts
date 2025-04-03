import { getServerSession } from "#auth";

export default eventHandler(async (event) => {
  const session = await getServerSession(event);

  if (
    (!session || session.user?.name != "talwat") &&
    !import.meta.server &&
    !event.path.startsWith("/api/auth")
  ) {
    throw createError({
      statusCode: 401,
      statusMessage: "Unauthorized",
    });
  }
});
