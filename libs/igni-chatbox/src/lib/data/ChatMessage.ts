import { User } from "./User";

export interface ChatMessage{
  id: number;
  user: User;
  message: string;
}
