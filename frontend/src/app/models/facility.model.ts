import { Floor } from "./floor.model"
import { User } from "./user.model";

export type Facility = {
  id: number,
  user?: User;
  floors: Floor[]
}