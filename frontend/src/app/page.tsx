"use client"

import {useEffect} from "react";

export default function Home() {
    useEffect(() => {
        (async () => {
            const res =
                await fetch("http://localhost:8080/api/v1/persons", {
                    method: "GET",
                    headers: {
                        "Authorization": "Basic " + "amigoscode:password"
                    }
                })
            console.log(res.status);
            const data = await res.json();
            console.log(data);


        })()
    })
    return "Hello World"
}
