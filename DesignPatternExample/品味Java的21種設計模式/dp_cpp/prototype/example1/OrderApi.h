#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace prototype
			{
				namespace example1
				{
					///
					/// <summary> * �����Ľӿ� </summary>
					/// 
					class OrderApi
					{
					///	
					///	 <summary> * ��ȡ������Ʒ���� </summary>
					///	 * <returns> �����в�Ʒ���� </returns>
					///	 
					public:
						virtual public int getOrderProductNum() = 0;
					///	
					///	 <summary> * ���ö�����Ʒ���� </summary>
					///	 * <param name="num"> ������Ʒ���� </param>
					///	 
						virtual public void setOrderProductNum(int) = 0;
					};

				}
			}
		}
	}
}