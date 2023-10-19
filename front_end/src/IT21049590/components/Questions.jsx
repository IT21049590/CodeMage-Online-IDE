// Question.js
import axios from "axios";
import { useParams, Link } from "react-router-dom";
import React, { useState, useEffect } from "react";
import Grid from "@mui/material/Grid";
import QuestionCard from "./QuestionCard";

const Question = ({}) => {
  const [questions, setQuestions] = useState([]);
  const { userId } = useParams();
  useEffect(() => {
    axios
      .get(`http://localhost:8080/questions/user/${userId}`)
      .then((response) => {
        setQuestions(response.data);

        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);
  console.log(questions[0]);

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "60%",
        marginLeft: "auto",
        marginRight: "auto",
        marginTop: "5rem",
      }}
    >
      <Grid container spacing={2}>
        {questions.map((question, index) => (
          <Grid item xs={12} key={index}>
            <QuestionCard question={question} />
          </Grid>
        ))}
      </Grid>
    </div>
  );
};

export default Question;
