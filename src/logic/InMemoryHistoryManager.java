// logic/InMemoryHistoryManager.java
package logic;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> taskMap = new HashMap<>();
    private final CustomLinkedList history = new CustomLinkedList();
    private static final int HISTORY_SIZE = 10;

    private static class Node {
        Task data;
        Node next;
        Node prev;

        Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private class CustomLinkedList {
        private Node head;
        private Node tail;
        private int size = 0;

        public void linkLast(Task task) {
            Node newNode = new Node(task);

            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }
            tail = newNode;

            taskMap.put(task.getId(), newNode);
            size++;

            if (size > HISTORY_SIZE) {
                removeFirst();
            }
        }

        public void removeNode(Node node) {
            if (node == null) {
                return; // Handle null node case
            }

            int taskId = node.data.getId();

            if (node == head && node == tail) {
                head = null;
                tail = null;
            } else if (node == head) {
                head = node.next;
                if (head != null) {
                    head.prev = null;
                }
            } else if (node == tail) {
                tail = node.prev;
                if (tail != null) {
                    tail.next = null;
                }
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            taskMap.remove(taskId);
            size--;
        }

        public void removeFirst() {
            if (head != null) {
                removeNode(head);

            }
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = head;
            while (current != null) {
                tasks.add(current.data);
                current = current.next;
            }
            return tasks;
        }
    }

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (taskMap.containsKey(taskId)) {
            Node existingNode = taskMap.get(taskId);
            history.removeNode(existingNode);
        }
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            Node nodeToRemove = taskMap.get(id);
            if (nodeToRemove != null) {
                history.removeNode(nodeToRemove);
            }
            taskMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}