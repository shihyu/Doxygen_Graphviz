#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace proxy
			{
				namespace example4
				{
					///
					/// <summary> * ��������Ľӿڶ��� </summary>
					/// 
					class OrderApi
					{
					///	
					///	 <summary> * ��ȡ���������Ĳ�Ʒ���� </summary>
					///	 * <returns> ���������Ĳ�Ʒ���� </returns>
					///	 
					public:
						virtual public std::string getProductName() = 0;
					///	
					///	 <summary> * ���ö��������Ĳ�Ʒ���� </summary>
					///	 * <param name="productName"> ���������Ĳ�Ʒ���� </param>
					///	 * <param name="user"> ������Ա </param>
					///	 
						virtual public void setProductName(std::string, std::string) = 0;
					///	
					///	 <summary> * ��ȡ�������������� </summary>
					///	 * <returns> �������������� </returns>
					///	 
						virtual public int getOrderNum() = 0;
					///	
					///	 <summary> * ���ö������������� </summary>
					///	 * <param name="orderNum"> �������������� </param>
					///	 * <param name="user"> ������Ա </param>
					///	 
						virtual public void setOrderNum(int, std::string) = 0;
					///	
					///	 <summary> * ��ȡ������������Ա </summary>
					///	 * <returns> ������������Ա </returns>
					///	 
						virtual public std::string getOrderUser() = 0;
					///	
					///	 <summary> * ���ô�����������Ա </summary>
					///	 * <param name="orderUser"> ������������Ա </param>
					///	 * <param name="user"> ������Ա </param>
					///	 
						virtual public void setOrderUser(std::string, std::string) = 0;
					};

				}
			}
		}
	}
}