import { createReadStream } from "fs";
import { mkdir, readdir, readFile } from "fs/promises";
import { DATA_PATH, NEWS_PATH } from "~/server/global";
import { createInterface } from "readline/promises";

export interface ListArticle {
  date: string;
  headline: string;
}

async function getFirstLine(path: string): Promise<string> {
  const stream = createReadStream(path);
  try {
    for await (const line of createInterface(stream)) return line;
    return ""; // If the file is empty.
  } finally {
    stream.destroy(); // Destroy file stream.
  }
}

export default defineEventHandler(async (event) => {
  await mkdir(DATA_PATH, { recursive: true });
  await mkdir(NEWS_PATH, { recursive: true });

  let files = await readdir(NEWS_PATH, {
    withFileTypes: true,
  });

  let articles: ListArticle[] = [];

  for (const file of files) {
    if (file.isDirectory()) {
      continue;
    }

    const [name, extension] = file.name.split(".", 2);
    if (extension != "txt") {
      continue;
    }

    const headline: string = await getFirstLine(
      `${file.parentPath}/${file.name}`,
    );

    articles.push({
      date: name,
      headline,
    });
  }

  articles.sort((a, b) => {
    return new Date(b.date).getTime() - new Date(a.date).getTime();
  });

  return articles;
});
