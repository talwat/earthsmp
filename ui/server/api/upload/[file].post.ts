import { writeFile, mkdir } from "fs/promises";
import type { Encoding } from "h3";
import { readRawBody, getQuery } from "h3";
import { getServerSession } from "#auth";
import { ALLOWED_FILES, DATA_PATH } from "~/server/global";

export default defineEventHandler(async (event) => {
  const encoding = (getQuery(event).encoding as Encoding | undefined) || false;
  const data = (await readRawBody(event, encoding))!;
  console.log(data);
  const file = event.context.params!.file;
  const [filename, extension] = file.split(".", 2);

  if (!ALLOWED_FILES.includes(file)) {
    throw createError({
      status: 401,
      statusMessage: "File cannot be edited",
    });
  }

  await mkdir(DATA_PATH, { recursive: true });

  const backup_path = `${DATA_PATH}/backups/${filename}`;
  await mkdir(backup_path, { recursive: true });

  await writeFile(`${DATA_PATH}/${file}`, data);

  const now = new Date();
  const name = `${filename}_${now.toISOString()}`;

  const image_name = `${name}.${extension}`;
  await writeFile(`${backup_path}/${image_name}`, data);

  const info_name = `${name}_info.txt`;
  const session = await getServerSession(event);
  const info = {
    user: session!.user!.name!,
    time: new Date().toISOString(),
  };

  await writeFile(`${backup_path}/${info_name}`, JSON.stringify(info));

  return;
});
