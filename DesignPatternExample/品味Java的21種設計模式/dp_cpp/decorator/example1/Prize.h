#pragma once

#include "TempDB.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example1
				{

					///
					/// <summary> * ���㽱��Ķ��� </summary>
					/// 
					class Prize
					{
					///	
					///	 <summary> * ����ĳ����ĳ��ʱ���ڵĽ�����Щ��������ʾ�в�����ʹ�ã�
					///	 * ������ʵ��ҵ��ʵ�����ǻ��õģ�Ϊ�˱�ʾ���Ǹ������ҵ�񷽷���
					///	 * �����Щ������������ </summary>
					///	 * <param name="user"> �����㽱�����Ա </param>
					///	 * <param name="begin"> ���㽱��Ŀ�ʼʱ�� </param>
					///	 * <param name="end"> ���㽱��Ľ���ʱ�� </param>
					///	 * <returns> ĳ����ĳ��ʱ���ڵĽ��� </returns>
					///	 
					public:
						virtual double calcPrize(std::string user, Date *begin, Date *end);
					///	
					///	 <summary> * ����ĳ�˵ĵ���ҵ�񽱽𣬲����ظ����Ͳ���ע���� </summary>
					///	 
					private:
						double monthPrize(std::string user, Date *begin, Date *end);
					///	
					///	 <summary> * ����ĳ�˵��ۼƽ��𣬲����ظ����Ͳ���ע���� </summary>
					///	 
					public:
						virtual double sumPrize(std::string user, Date *begin, Date *end);
					///	
					///	 <summary> * �ж���Ա����ͨ��Ա����ҵ���� </summary>
					///	 * <param name="user"> ���жϵ���Ա </param>
					///	 * <returns> true��ʾ��ҵ����,false��ʾ����ͨ��Ա </returns>
					///	 
					private:
						bool isManager(std::string user);
					///	
					///	 <summary> * ���㵱���Ŷ�ҵ�񽱣������ظ����Ͳ���ע���� </summary>
					///	 
					public:
						virtual double groupPrize(std::string user, Date *begin, Date *end);
					};

				}
			}
		}
	}
}