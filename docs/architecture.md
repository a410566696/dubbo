# Apache Dubbo Architecture Overview

## Introduction

This document describes the core architecture and request processing flow of Apache Dubbo.

The purpose of this document is to help developers understand the internal design of Dubbo, including:

- RPC invocation process
- Service discovery mechanism
- Cluster fault tolerance
- Load balancing
- Protocol abstraction
- Network transport
- Extension mechanism

This document is maintained as part of this development fork to improve source code understanding and provide a reference for future development and contributions.

---

# 1. High-Level Architecture

Apache Dubbo adopts a layered architecture design.

The main invocation path is:

```text
Consumer Application

        |
        v

      Proxy

        |
        v

    Invoker

        |
        v

    Cluster

        |
        v

   Directory

        |
        v

     Router

        |
        v

  LoadBalance

        |
        v

    Protocol

        |
        v

   Exchange

        |
        v

   Transport

        |
        v
