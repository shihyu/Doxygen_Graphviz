#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example2
				{
					///
					/// <summary> * ��������ģ�Ͷ��� </summary>
					/// 
					class PayModel
					{
					///	
					///	 <summary> * ֧�����ʵ���Ա </summary>
					///	 
					private:
						std::string userName;
					///	
					///	 <summary> * ֧���Ĺ������� </summary>
					///	 
						double pay;
					public:
						virtual std::string getUserName();
						virtual void setUserName(std::string userName);
						virtual double getPay();
						virtual void setPay(double pay);
						virtual std::string ToString();
					};
				}
			}
		}
	}
}