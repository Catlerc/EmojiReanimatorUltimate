import React, { useState, useEffect, useCallback, useRef } from "react";
import Rete from "rete";
import { createRoot } from "react-dom/client";
import ReactRenderPlugin from "rete-react-render-plugin";
import ConnectionPlugin from "rete-connection-plugin";
import AreaPlugin from "rete-area-plugin";
import { MyNode } from "./MyNode";
import {KekNode, numSocket} from "./nodes";
import {NumControl} from "./NumControl";

// var numSocket = new Rete.Socket("Number value");



class NumComponent extends Rete.Component {
    constructor() {
        super("Number");
    }

    builder(node) {
        var out1 = new Rete.Output("num", "Number", numSocket);
        var ctrl = new NumControl(this.editor, "num", node);

        return node.addControl(ctrl).addOutput(out1);
    }

    worker(node, inputs, outputs) {
        outputs["num"] = node.data.num;
    }
}

class AddComponent extends Rete.Component {
    constructor() {
        super("Add");
        // this.data.component = MyNode; // optional
    }

    builder(node) {
        var inp1 = new Rete.Input("num1", "Number", numSocket);
        var inp2 = new Rete.Input("num2", "Number2", numSocket);
        var out = new Rete.Output("num", "Number", numSocket);

        inp1.addControl(new NumControl(this.editor, "num1", node));
        inp2.addControl(new NumControl(this.editor, "num2", node));

        return node
            .addInput(inp1)
            .addInput(inp2)
            .addControl(new NumControl(this.editor, "preview", node, true))
            .addOutput(out);
    }

    worker(node, inputs, outputs) {
        var n1 = inputs["num1"].length ? inputs["num1"][0] : node.data.num1;
        var n2 = inputs["num2"].length ? inputs["num2"][0] : node.data.num2;
        var sum = n1 + n2;

        this.editor.nodes
            .find((n) => n.id == node.id)
            .controls.get("preview")
            .setValue(sum);
        outputs["num"] = sum;
    }
}

export async function createEditor(container) {
    var components = [new NumComponent(), new AddComponent(), new KekNode()];

    var editor = new Rete.NodeEditor("demo@0.1.0", container);
    editor.use(ConnectionPlugin);
    editor.use(ReactRenderPlugin, { createRoot });
    // editor.use(Context);

    var engine = new Rete.Engine("demo@0.1.0");

    components.map((c) => {
        editor.register(c);
        engine.register(c);
    });

    var n1 = await components[0].createNode({ num: 2 });
    var n2 = await components[0].createNode({ num: 3 });
    var add = await components[1].createNode();
    var kek = await components[2].createNode();

    n1.position = [80, 200];
    n2.position = [80, 400];
    add.position = [500, 240];
    kek.position = [600, 240];

    editor.addNode(n1);
    editor.addNode(n2);
    editor.addNode(add);
    editor.addNode(kek);

    editor.connect(n1.outputs.get("num"), add.inputs.get("num1"));
    editor.connect(n2.outputs.get("num"), add.inputs.get("num2"));

    editor.on(
        "process nodecreated noderemoved connectioncreated connectionremoved",
        async () => {
            console.log("process");
            await engine.abort();
            await engine.process(editor.toJSON());
        }
    );

    editor.view.resize();
    editor.trigger("process");
    AreaPlugin.zoomAt(editor, editor.nodes);

    return editor;
}

export function useRete() {
    const [container, setContainer] = useState(null);
    const editorRef = useRef();

    useEffect(() => {
        if (container) {
            createEditor(container).then((value) => {
                console.log("created");
                editorRef.current = value;
            });
        }
    }, [container]);

    useEffect(() => {
        return () => {
            if (editorRef.current) {
                console.log("destroy");
                editorRef.current.destroy();
            }
        };
    }, []);

    return [setContainer];
}
