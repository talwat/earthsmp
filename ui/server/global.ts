export const DATA_PATH = `${process.env.SERVER_PATH}/plugins/EarthSMP`;
export const ALLOWED_FILES = ["borders.png", "nations.yml"];
export const PAGES = [
  ["/news", "News"],
  ["/borders", "Borders"],
  ["/nations", "Nations"],
];
export const PAGES_JOURNALIST = [["/news", "News"]];

export const NEWS_PATH = `${DATA_PATH}/news`;
export const NEWS_BACKUP_PATH = `${DATA_PATH}/news/backup`;

export interface User {
  name: string;
  role: string;
}
