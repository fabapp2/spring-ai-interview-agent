Please create a UI for my Spring AI Interview Agent application.

It should be built with React in TypeScript and have the following specifications:

1. A Single Page Application (SPA) that is split into two sections:
    - On the left: A chat client with a multiline text field for input and message history display
    - On the right: A tree view for JSON documents in MongoDB

2. The chat on the left should communicate with my Spring AI Interview Agent backend and might trigger changes in
   MongoDB collections.

3. To visualize the changes in MongoDB triggered by messages, the right panel should:
    - Show the JSON document structure in a tree view
    - Use visual effects to highlight fields that change
    - Update in real-time as documents are modified

4. The UI should specifically focus on two MongoDB collections:
    - career_data: Contains user profile information
    - interview_plan: Contains the structure and progress of interviews

5. Technical requirements:
    - React with TypeScript
    - Material UI for components
    - Server-Sent Events (SSE) for real-time updates
    - JSON tree visualization

6. The goal is to help users visualize how the prompts sent in the chat (and the resulting tool calls) change
   documents in MongoDB, creating a debugging and demonstration tool.

Please generate a complete implementation including all necessary files, components, services, and
configurations.

This prompt provides clear direction for Claude to recreate the UI with all the key components and functionality.
It defines the two-panel layout, specifies the real-time visualization requirements, mentions the key MongoDB
collections, and lists the technical requirements including React, TypeScript, Material UI, and SSE.