import { mkdir, unlink } from "fs/promises";
import { DATA_PATH, NEWS_PATH } from "~/server/global";

export default defineEventHandler(async (event) => {
  await mkdir(DATA_PATH, { recursive: true });
  await mkdir(NEWS_PATH, { recursive: true });

  let article = event.context.params!.article;
  let [year, month, day] = article.split("-", 3);

  if (day.startsWith("0")) {
    day = day.slice(1);
  }

  if (month.startsWith("0")) {
    month = month.slice(1);
  }

  let path = `${NEWS_PATH}/${year}-${month}-${day}.txt`;
  try {
    await unlink(path);
  } catch {
    throw createError({
      status: 404,
      message: "Article either doesn't exist or can't be deleted",
    });
  }
});
