#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example6
				{
					///
					/// <summary> * ��װ���۵������ݣ��򵥵�ʾ��һЩ </summary>
					/// 
					class SaleModel
					{
					///	
					///	 <summary> * ���۵���Ʒ </summary>
					///	 
					private:
						std::string goods;
					///	
					///	 <summary> * ���۵����� </summary>
					///	 
						int saleNum;
					public:
						virtual std::string getGoods();
						virtual void setGoods(std::string goods);
						virtual int getSaleNum();
						virtual void setSaleNum(int saleNum);

						virtual std::string ToString();
					};
				}
			}
		}
	}
}