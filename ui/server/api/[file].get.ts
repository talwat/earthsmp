import { readFile } from "fs/promises";
import { ALLOWED_FILES, DATA_PATH } from "~/server/global";

export default defineEventHandler(async (event) => {
  const file = event.context.params!.file;

  if (!ALLOWED_FILES.includes(file)) {
    throw createError({
      status: 401,
      statusMessage: "File cannot be viewed",
    });
  }

  try {
    return readFile(`${DATA_PATH}/${file}`, "utf8");
  } catch {
    throw createError({
      statusCode: 404,
      statusMessage: "Requested file does not exist",
    });
  }
});
