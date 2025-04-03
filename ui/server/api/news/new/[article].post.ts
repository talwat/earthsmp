import { mkdir, stat, writeFile } from "fs/promises";
import { DATA_PATH, NEWS_PATH } from "~/server/global";

function verify(str: string) {
  let number = Number(str);
  return !isNaN(number) && str != "" && number > 0;
}

interface Article {
  headline: String;
}

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

  if (
    !verify(year) ||
    year.length !== 4 ||
    !verify(month) ||
    month.length > 2 ||
    !verify(day) ||
    day.length > 2
  ) {
    throw createError({
      status: 401,
      message: "Invalid Date",
    });
  }

  let path = `${NEWS_PATH}/${year}-${month}-${day}.txt`;
  let error = false;

  try {
    let exists = await stat(path);
    if (exists) {
      error = true;
    }
  } catch {
    let body: Article = await readBody(event);
    await writeFile(path, `${body.headline}`);
  }

  if (error) {
    throw createError({
      message: "Article already exists",
    });
  }
});
