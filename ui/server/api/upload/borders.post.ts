import { writeFile, mkdir } from "fs/promises"
import { readRawBody } from "h3";
import { BACKUP_PATH, DATA_PATH } from "~/server/paths";

export default defineEventHandler(async (event) => {
  const data = (await readRawBody(event, false))!;

  await mkdir(DATA_PATH, { recursive: true })
  await mkdir(BACKUP_PATH, { recursive: true })
  
  await writeFile(`${DATA_PATH}/borders.png`, data)

  const now = new Date()
  const name = `borders_${now.getUTCFullYear()}-${now.getUTCMonth()+1}-${now.getUTCDate()}`

  const image_name = `${name}.png`
  await writeFile(`${BACKUP_PATH}/${image_name}`, data)

  const info_name = `${name}_info.txt`
  await writeFile(`${BACKUP_PATH}/${info_name}`, "")

  return
});
