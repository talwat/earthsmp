import { mkdir, stat, readFile } from "fs/promises";
import { DATA_PATH, NEWS_PATH } from "~/server/global";

function verify(str: string) {
  let number = Number(str);
  return !isNaN(number) && str != "" && number > 0;
}

interface Article {
  headline: String;
  body: String;
  date: String;
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

  let date = `${year}-${month}-${day}`;

  try {
    let file = await readFile(`${NEWS_PATH}/${date}.txt`, "utf8");
    let firstLine = file.indexOf("\n");
    return {
      headline: file.substring(0, firstLine),
      body: file.substring(firstLine).trim(),
      date,
    } as Article;
  } catch {
    throw createError({
      statusCode: 404,
      statusMessage: `There is no article for ${date}`,
    });
  }
});
