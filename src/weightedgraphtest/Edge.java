/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weightedgraphtest;

public class Edge {

    int u;
    int v;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override // Test if two edges are identical
    public boolean equals(Object o) {
        return u == ((Edge) o).u && v == ((Edge) o).v;
    }
}
