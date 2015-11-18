/**
 * 
 */
package com.nyt.mpt.proposal;


/**
 * @author surendra.singh
 *
 */
public class SequenceGenaratorTest {

	public static void main(String[] args) {

		final int positionToMove = -5;
		
		final long[] lineItemList = new long[] {1, 2, 3, 4, 5, 6, 7, 8};

		final long[] lineItemToMove = new long[] {4L, 7L};
		
		reOrderBeans(lineItemList, lineItemToMove, positionToMove);
	}

	private static void reOrderBeans(final long[] lineItemList, final long[] lineItemToMove, int positionToMove) {
		final long[] finalSeqArr = new long[lineItemList.length];
		for (int j = 0; j < lineItemToMove.length; j++) {
			for (int i = 0; i < lineItemList.length; i++) {
				long lineItem = lineItemToMove[j];
				if (lineItem == lineItemList[i]) {
					int newSeq = i + positionToMove;
					if(positionToMove > 0) {
						if (newSeq + lineItemToMove.length - j >= finalSeqArr.length) {
							finalSeqArr[finalSeqArr.length - (lineItemToMove.length - j)] = lineItem;
						} else {
							finalSeqArr[newSeq] = lineItem;
						}
					} else {
						if (newSeq <= 0) {
							finalSeqArr[j] = lineItem;
						} else {
							finalSeqArr[newSeq] = lineItem;
						}
					}
					lineItemList[i] = 0;
				}
			}
		}
		for (int i = 0; i < lineItemList.length; i++) {
			if (lineItemList[i] != 0) {
				for (int j = 0; j < finalSeqArr.length; j++) {
					if(finalSeqArr[j] == 0) {
						finalSeqArr[j] = lineItemList[i];
						break;
					}					
				}				
			}			
		}
		
		for (long l : finalSeqArr) {
			System.out.print(l + ", ");
			
		}
	}
}

class Bean implements Comparable<Bean>{
	
	private int id;
	
	private int seq;

	public Bean(int id, int seq) {
		super();
		this.id = id;
		this.seq = seq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	@Override
	public int compareTo(Bean o) {
		return Long.valueOf(this.seq).compareTo(Long.valueOf(o.getSeq()));
	}	
}
