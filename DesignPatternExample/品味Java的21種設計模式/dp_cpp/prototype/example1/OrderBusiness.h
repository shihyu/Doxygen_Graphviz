#pragma once

#include "OrderApi.h"
#include "PersonalOrder.h"
#include "EnterpriseOrder.h"

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
					/// <summary> * ��������ҵ����� </summary>
					/// 
					class OrderBusiness
					{
					///	
					///	 <summary> * ���������ķ��� </summary>
					///	 * <param name="order"> �����Ľӿڶ��� </param>
					///	 
					public:
						virtual void saveOrder(OrderApi *order);

					//	public void saveOrder2(OrderApi order){
					//		int oldNum = order.getOrderProductNum();
					//		while(oldNum > 1000){
					//			//����һ����ʾ����ֳ������¶�������
					//			OrderApi newOrder = null;
					//			
					//			if(order instanceof PersonalOrder){
					//				//������Ӧ�Ķ�������
					//				PersonalOrder p2 = new PersonalOrder();
					//				//Ȼ����и�ֵ�ȣ�ʡ����
					//				//Ȼ�������ø�newOrder
					//				newOrder = p2;
					//			}else if(order instanceof EnterpriseOrder){
					//				//������Ӧ�Ķ�������
					//				EnterpriseOrder e2 = new EnterpriseOrder();
					//				//Ȼ����и�ֵ�ȣ�ʡ����
					//				//Ȼ�������ø�newOrder
					//				newOrder = e2;
					//			}			
					//			//Ȼ����в�ֺ�����ҵ���ܴ���ʡ����
					//		}		
					//	}
					};

				}
			}
		}
	}
}