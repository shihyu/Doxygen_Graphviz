#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example6
				{

					///
					/// <summary> * �������ݿ�ķ���ʹ�õĻص��ӿ� </summary>
					/// 
					class UpdateDBCallBack
					{
					///	
					///	 <summary> * ʹ��PreparedStatement����sql�е�"?"���� </summary>
					///	 * <param name="pstmt"> PreparedStatement ���� </param>
					///	 * <exception cref="Exception"> </exception>
					///	 
					public:
						virtual public void setValue(PreparedStatement*) throw(Exception) = 0;
					};

				}
			}
		}
	}
}