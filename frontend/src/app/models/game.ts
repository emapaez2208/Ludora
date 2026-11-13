export interface Game {
    externalId: string;
    name: string;
    frontPage: string;
    price: number;
    developer: string;
    description: string;
    releaseDate: Date;
    ageRange: string;
    genres: string[];
    statusBlocked: boolean;
    needRevision?: boolean;
}

