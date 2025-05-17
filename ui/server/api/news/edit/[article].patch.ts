import { mkdir, writeFile, access, constants } from "fs/promises";
import { DATA_PATH, NEWS_BACKUP_PATH, NEWS_PATH, User } from "~/server/global";

interface Article {
  headline: String;
  body: String;
}

export default defineEventHandler(async (event) => {
  await mkdir(DATA_PATH, { recursive: true });
  await mkdir(NEWS_PATH, { recursive: true });
  await mkdir(NEWS_BACKUP_PATH, { recursive: true });

  let article = event.context.params!.article;
  let [year, month, day] = article.split("-", 3);

  if (day.startsWith("0")) {
    day = day.slice(1);
  }

  if (month.startsWith("0")) {
    month = month.slice(1);
  }

  let path = `${NEWS_PATH}/${year}-${month}-${day}.txt`;
  let backup_path = `${NEWS_BACKUP_PATH}/${new Date().toISOString()}`;

  const { user } = await getUserSession(event);
  const userData = user as User;

  const info = {
    user: userData.name,
    time: new Date().toISOString(),
  };

  let body: Article = await readBody(event);
  let content = `${body.headline}\n\n${body.body}`;

  try {
    await access(path, constants.F_OK);

    await writeFile(path, content);
    await writeFile(`${backup_path}.txt`, content);
    await writeFile(`${backup_path}_info.txt`, JSON.stringify(info));
  } catch {
    throw createError({
      status: 404,
      message: "Article either doesn't exist or can't be written to",
    });
  }
});
