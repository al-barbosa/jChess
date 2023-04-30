package jchess.pieces;

public enum Alliance {
	WHITE {
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return -1;
		}
	},
	BLACK {
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return 1;
		}
	};
	
	public abstract int getDirection();
}
